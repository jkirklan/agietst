#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_control = "agie_inbound/agie_inbound_control"


def intf_up(msg_list):
	print msg_list
	print 'Uppy '

def intf_down(msg_list):
        print msg_list
        print 'Downer '


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
			msg_list = received.split(',')	
			if msg_list[0] == 'up':
				intf_up(msg_list)
			elif msg_list[0] == 'down':
				intf_down(msg_list)
			else:
				print "freakout"
			session.acknowledge()
        except MessagingError,m:
                print m
        finally:
                lb_connection.close()

broker_conn()
