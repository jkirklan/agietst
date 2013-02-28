#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_control = "agie_inbound/agie_inbound_control"
intf_name = "eth4"
intf_ip = "10.1.10.33"

# create connection to local broker
lb_connection = Connection(broker_local)
try:
	lb_connection.open()
	session = lb_connection.session()
	sender = session.sender(addr_control)
	msg_content = "up," + intf_name + ',' + intf_ip
	msg = Message(msg_content)
	print msg_content
	sender.send(msg)
except MessagingError,m:
	print m
finally:
	lb_connection.close()

