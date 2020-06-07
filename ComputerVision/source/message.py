import json


class Message:
    """Represents a message to be to and from the server"""

    _code: int = None # Identifier (must be positive)
    _data: str = None # Data in JSON string format
    
    def __init__(self, code: int, data: str):

        # Check code validity
        if not type(code) is int or code < 1:
            raise ValueError("Argument 'code' must be a int larger than 0")
        self._code = code

        # Parse JSON and check validity
        try:
            json.loads(data)
        except (json.JSONDecodeError, TypeError):
            raise ValueError("Argument 'data' must be a JSON formatted string")
        self._code = code

        # Parse JSON and check validity
        try:
            json.loads(data)
        except (json.JSONDecodeError, TypeError):
            raise ValueError("Argument 'data' must be a JSON formatted string")
        self._data = data


    def __str__(self):
        return f"Message( code={self._code}, data={self._data} )"


    def to_json(self):
        return json.dumps({"code" : self._code, "data" : json.loads(self._data)})

    @staticmethod
    def from_json(json_msg: str):
        dict_msg = json.loads(json_msg)
        return Message(dict_msg["code"], json.dumps(dict_msg["data"]))



if __name__ == "__main__":
    msg = Message(1, '{"num" : 10}')
    print(Message.from_json(msg.to_json()))
    