#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_control = "agie_inbound/agie_inbound_control"
intf_table = []
title = ['status', 'intf_name', 'intf_ip']

def intf_up(msg_list,intf_table):
	tmp_tbl_up = intf_table
	print 'add starting on ', tmp_tbl_up
	tmp_entry = dict(zip(title,msg_list))
	print 'tmp_entry', tmp_entry
	intf_tmp = tmp_entry.get('intf_name')
	print 'intf', intf_tmp
	exist = [ iface for iface in tmp_tbl_up if iface.get('intf_name') == intf_tmp ]
	if exist:
		print "already exists"
		print 'returning', intf_table
		return intf_table
	else:
		tmp_tbl_up.append(tmp_entry)
		print 'Added inteface on ', msg_list[1]
		print tmp_tbl_up
		return tmp_tbl_up
	

def intf_down(msg_list, intf_table):
	tmp_tbl = intf_table
	tmp_entry = dict(zip(title,msg_list))
	intf_to_rm = tmp_entry['intf_name']	
	print 'removing ', intf_to_rm
	up = [ iface for iface in tmp_tbl if iface.get('intf_name') != intf_to_rm ]
        print 'Inteface removed ', intf_to_rm
	tmp_tbl = up
	print 'returning ', tmp_tbl
	return tmp_tbl


def broker_conn():
# create connection to local broker
        lb_connection = Connection(broker_local)
	intf_table = []
        try:
                lb_connection.open()
                session = lb_connection.session()
		receiver = session.receiver("agie_inbound_control")
		while True:
			print 'initial intf_table', intf_table
			message = receiver.fetch()
			received = message.content
			print 'received', received
			msg_list = received.split(',')	
			if msg_list[0] == 'up':
				intf_table = intf_up(msg_list, intf_table)
				print 'up event', intf_table
			elif msg_list[0] == 'down':
				intf_table = intf_down(msg_list, intf_table)
				print 'downer man', intf_table
			else:
				print "freakout"
			session.acknowledge()
        except MessagingError,m:
                print m
        finally:
                lb_connection.close()

broker_conn()
