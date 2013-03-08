#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_data = "outbound_agie_eth5"
pri = 1
msg_content = "hello wolrd"
def get_move():
	message = receiver.fetch()
	received = message.content
	print "moving message:", message 
	sender2.send(message)
	print 'moved', received
	session.acknowledge()

# create connection to local broker
lb_connection = Connection(broker_local)
try:
	lb_connection.open()
	session = lb_connection.session()
	receiver = session.receiver("agie_inbound_data")
	sender2 = session.sender("outbound_agie_eth5; {create:always, node:{x-declare:{auto-delete:True, alternate-exchange:agie_inbound_d}}}")
	get_move()
except MessagingError,m:
	print m
finally:
	lb_connection.close()

