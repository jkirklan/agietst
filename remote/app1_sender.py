#!/usr/bin/python
import sys
import json
from qpid.messaging import *

connection = Connection("localhost:5672")

try:
  connection.open()
  session = connection.session()

  sender = session.sender("agie_inbound_d")

  x = sys.argv[1]
  y = sys.argv[2]

  jsonMsg = json.dumps({'x': x, 'y': y})
  print jsonMsg
  msg = Message()
  msg.subject = 'app1'
  msg.content = jsonMsg

  sender.send(msg)
except MessagingError,m:
  print m


connection.close()
