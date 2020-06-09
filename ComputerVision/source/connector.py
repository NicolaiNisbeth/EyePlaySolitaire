import socket
import queue
import threading
import json

from PIL import Image
from message import Message

class Connector:

    def __init__(self, port: int, message_listener):
        self._queued_messages = queue.Queue()
        self._dequeue_event_map = {}

        # Connect to server
        self._connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._connection.connect(("localhost", port))
        print("Connected successfully!")

        self._message_listener = message_listener

        threading._start_new_thread(self._send_messages, ())
        threading._start_new_thread(self._recieve_messages, ())


    def send_message(self, msg: Message, wait=False):
        """
        Enqueue a message to be sent to the server
        
        Parameters:
            msg (Message): The message to be sent
            wait (bool): If the Thread should wait for the message to be put into the buffer (not necessarily sent and recieved)
        
        """
        
        if wait:
            # If we should wait for message to be sent, we need to create an
            # Event to wait on. This should be done before adding the message
            # to prevent deadlock.
            self._dequeue_event_map[msg] = threading.Event()

        # Enqueue message
        self._queued_messages.put(msg)
        
        if wait:
            # Wait for message and remove deuquee event from map
            self._dequeue_event_map[msg].wait()
            self._dequeue_event_map.pop(msg)

    # Loop which keeps checking the message queue, and send
    # any existing messages
    def _send_messages(self):

        while True:
            # Send the message
            msg = self._queued_messages.get() # blocks until something is in the queue
            self._connection.sendall((msg.to_json() + "\n").encode())
            print("message sent")
            # Signal that the message has been sent
            if msg in self._dequeue_event_map:
                self._dequeue_event_map[msg].set()


    def _recieve_messages(self):
        connection_file = self._connection.makefile()
        while True:
            str_msg = connection_file.readline()
            msg = None
            try:
                msg = Message.from_json(str_msg)
                print("Received message: ", msg )
            except json.JSONDecodeError as e:
                print(f"Couldn't deserialize message {str_msg} as JSON {e.msg}")

            if msg is not None:
                self._message_listener(msg)

                
                
            
