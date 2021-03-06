#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_control = "agie_inbound_d/agie_inbound_data"
pri = 1
iter_t = sys.argv[1]
iter = int(iter_t)
seq_list = list(xrange(iter))

# create connection to local broker
lb_connection = Connection(broker_local)
try:
	lb_connection.open()
	session = lb_connection.session()
	sender = session.sender(addr_control)
	for seq in seq_list:
		msg_content = "hello world %i" % seq
		msg = Message(msg_content)
		print seq,iter
		print 'sending msg with content:', msg_content
		sender.send(msg)
except MessagingError,m:
	print m
finally:
	lb_connection.close()

