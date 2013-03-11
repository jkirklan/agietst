#!/usr/bin/python
import sys
import logging
from qpid.messaging import *

#global vars
logging.basicConfig(filename="/tmp/agie.log", level=logging.INFO)
broker_local = "localhost:5672"
addr_control = "agie_inbound/agie_inbound_control"
addr_data_src = "agie_inbound_d/agie_inbound_data"
net1_q = "agie_data_net1"
net2_q = "agie_data_net2"
eth4Queue = None
eth5Queue = None
qpid_opt = "; {create:always, node:{x-declare:{auto-delete:true, alternate-exchange: 'amq.direct'}}}"
net1_con = '"' + net1_q + qpid_opt + '"'
intf_table = []
title = ['status', 'intf_name', 'intf_ip', 'broker', 'queue']

def intf_up(msg_list,intf_table, session):
	tmp_tbl_up = intf_table
	logging.debug('add starting on %s' % (tmp_tbl_up))
	tmp_entry = dict(zip(title,msg_list))
	logging.info('tmp_entry: %s' % (tmp_entry))
	intf_tmp = tmp_entry.get('intf_name')
	exist = [ iface for iface in tmp_tbl_up if iface.get('intf_name') == intf_tmp ]
	if exist:
		print "Interface already exists."
		#print 'returning', intf_table
		return intf_table
	else:
		if tmp_entry['intf_name'] == "eth4":
			global eth4Queue
			eth4Queue = session.receiver("agie_data_net1; {create:always, node:{x-declare:{auto-delete:true, alternate-exchange: 'amq.direct'}}}")
			tmp_tbl_up.append(tmp_entry)
		elif tmp_entry['intf_name'] == "eth5":
			global eth5Queue
			eth5Queue = session.receiver("agie_data_net2; {create:always, node:{x-declare:{auto-delete:true, alternate-exchange: 'amq.direct'}}}")
			tmp_tbl_up.append(tmp_entry)
		else:
			print "major major issue"
		print 'Added inteface on ', msg_list[1]
		#print tmp_tbl_up
		return tmp_tbl_up
	

def intf_down(msg_list, intf_table, eth4Queue, eth5Queue):
	tmp_tbl = intf_table
	tmp_entry = dict(zip(title,msg_list))
	intf_to_rm = tmp_entry['intf_name']	
	if intf_to_rm == 'eth4':
		eth4Queue.close()
	elif intf_to_rm == 'eth5':
		eth5Queue.close()
	else:
		print "queue close error"
	print 'Deteched down network.  Removing interface:', intf_to_rm
	up = [ iface for iface in tmp_tbl if iface.get('intf_name') != intf_to_rm ]
        print 'Inteface removed ', intf_to_rm
	tmp_tbl = up
	#print 'returning ', tmp_tbl
	return tmp_tbl

def data_msg_mover(receiver_d, eth4Queue, eth5Queue):
        message = receiver.fetch()
        received = message.content
        print "moving message:", message
        sender2.send(message)
        print 'moved', received
        session.acknowledge()

def broker_conn():
# create connection to local broker
        lb_connection = Connection(broker_local)
        try:
                lb_connection.open()
                session = lb_connection.session()
		receiver = session.receiver("agie_inbound_control")
		return receiver
        except MessagingError,m:
                print m
def intf_change(intf_table):
	#print 'initial intf_table', intf_table
	message = receiver.fetch()
	received = message.content
	#print 'received', received
	msg_list = received.split(',')
	if msg_list[0] == 'up':
		intf_table = intf_up(msg_list, intf_table, session)
		print 'Up event received:', intf_table
		session.acknowledge()
		return intf_table
	elif msg_list[0] == 'down':
		intf_table = intf_down(msg_list, intf_table, eth4Queue, eth5Queue)
		print 'Down event received:', intf_table
		session.acknowledge()	
		return intf_table
	else:  
		print "freakout"

#main()
lb_connection = Connection(broker_local)
intf_table = []
try:   
	lb_connection.open()
	session = lb_connection.session()
	receiver = session.receiver("agie_inbound_control")
	receiver_d = session.receiver(addr_data_src)
	#sender_4 = session.sender(
	#after up/down then send and receive data messages.
except MessagingError,m:
	print m

while True:
	intf_table = intf_change(intf_table)
	
