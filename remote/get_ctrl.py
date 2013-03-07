#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_control = "agie_inbound/agie_inbound_control"
intf_table = []
title = ['status', 'intf_name', 'intf_ip']

def intf_up(msg_list):
	tmp_tbl = intf_table
	tmp_entry = dict(zip(title,msg_list))
	print 'tmp_entry', tmp_entry
	intf_tmp = tmp_entry.get('intf_name')
	print 'intf', intf_tmp
	exist = [ iface for iface in tmp_tbl if iface.get('intf_name') == intf_tmp ]
	if exist:
		print "already exists"
	else:
		tmp_tbl.append(tmp_entry)
	print 'Added inteface on ', msg_list[1]
	print tmp_tbl
	return tmp_tbl
	

def intf_down(msg_list):
	tmp_tbl = intf_table
	tmp_entry = dict(zip(title,msg_list))
	intf_to_rm = tmp_entry['intf_name']	
	up = [ iface for iface in intf_table if iface.get('intf_name') != intf_to_rm ]
	print up
        print 'Inteface removed '
	return tmp_tbl


def broker_conn():
# create connection to local broker
        lb_connection = Connection(broker_local)
        try:
                lb_connection.open()
                session = lb_connection.session()
		receiver = session.receiver("agie_inbound_control")
		while True:
			message = receiver.fetch()
			received = message.content
			print 'received', received
			session.acknowledge()
        except MessagingError,m:
                print m
        finally:
                lb_connection.close()

broker_conn()
