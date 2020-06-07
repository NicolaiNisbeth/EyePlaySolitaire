
import sys
import json
import time

from message import Message
from connector import Connector



def main():

    # Get port argument
    if len(sys.argv) < 2:
        raise Exception("Missing port argument")
    port = None
    try:
        port = int(sys.argv[1])
        if type(port) is not int or port < 1 or port > 65535:
            raise Exception()
    except:
        raise TypeError("Port argument but be integer larger between 1 and 65535")

    connector = Connector(port)
    f =  open("C:/Users/malte/IdeaProjects/EyePlaySolitaire/ComputerVision/testfile.txt", "r")
    data = f.read()
    f.close()
    len(data)


    #connector.send_message(Message(100, json.dumps({"msg" : data})), True)
    #print("Sent first message")

    connector.send_message(Message(101, json.dumps({"num" : 19})), True)
    print("Sent second message")
    connector.send_message(Message(102, json.dumps({"correct" : True})), True)
    print("Sent third message")

    time.sleep(15)
    



if __name__ == "__main__":
    main()