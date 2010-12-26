package com.cthulupus.mongrel2.examples;

import org.json.JSONException;
import org.json.JSONObject;

import com.cthulupus.mongrel2.Connection;
import com.cthulupus.mongrel2.Request;

public class TestHandler2 {

	static Integer UUIDMODE = 0;
	static Integer IDMODE = 1;
	static Integer PATHMODE = 2;
	static Integer LENGTHMODE = 3;

	public static void main(String[] args) throws JSONException {
		
		String subAddr = "tcp://127.0.0.1:9997";
		String pubAddr = "tcp://127.0.0.1:9996";
		
		Connection conn = new Connection("a", subAddr, pubAddr);

		while (true) {
			System.out.println("Waiting for request");
			Request req = conn.recv();

			System.out.println("uuid=" + req.getUuid());
			System.out.println("id=" + req.getId());
			System.out.println("path=" + req.getPath());
			System.out.println("length=" + req.getLength());
			
			JSONObject json = new JSONObject(new String(req.getRest()));
			System.out.println(json.toString(4));


			conn.send(req.getUuid(), req.getId().toString(), "hello");

		}
	}

}
