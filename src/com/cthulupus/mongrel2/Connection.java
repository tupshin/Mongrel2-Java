package com.cthulupus.mongrel2;

import java.util.Hashtable;

import org.zeromq.ZMQ;

public class Connection {

	private ZMQ.Context ctx;
	private ZMQ.Socket reqs;
	private ZMQ.Socket resp;
	public Connection(String senderID, String subAddr, String pubAddr) {

		ctx = ZMQ.context(1);
		reqs = ctx.socket(ZMQ.PULL);

		reqs.connect(subAddr);

		resp = ctx.socket(ZMQ.PUB);
		resp.connect(pubAddr);
		//	        resp.setsockopt("a", senderID);

	}

	String HTTP_FORMAT = "HTTP/1.1 %(code)s %(status)s\r\n%(headers)s\r\n\r\n%(body)s";

	Integer	MAX_IDENTS = 100;

	public String httpResponse(String body, Integer code, String status, Hashtable<String,String> headers) {
		//			 Hashtable<String,String> payload = new Hashtable<String,String>() {{
		//			    put("code",      code);
		//			    put("status",      status);
		//			    put("body",    body);
		//			 }};
		//
		//			
		//		    headers.put("Content-Length",len(body));
		//		    Enumeration<String> keys = headers.keys();
		//		    while (keys.hasMoreElements()) {
		//		    	String key = keys.nextElement();
		//		    	payload.put
		//		    }
		//		    payload.put("headers", "\r\n".join('%s: %s' % (k,v) for k,v in
		//		                                     headers.items())

		return ""; // HTTP_FORMAT % payload;
	}


	public Request recv() {
		return Request.parse(reqs.recv(0));
	}

	//	public Request recvJSON() throws JSONException {
	//		
	//		Request req = recv();
	//
	//	        if (!req.data) {
	//	        	JSONObject json = new JSONObject(req.body);
	//	            req.jsonData = json;
	//	        }
	//
	//	        return req;
	//
	//	}

	public void send(String uuid, String connID, String msg) {

		String header = uuid + " " + connID.length() + " " + connID;
		resp.send((header + " " + msg).getBytes(), 1);

	}

	public void reply (Request req, String msg) {
		send(req.sender, req.connID, msg);

	}

	//	public void reply (Request req, JSONObject data) throws JSONException {
	//		send(req.sender, req.connID, data.toString(0));
	//
	//	}

	public void replyHTTP(Request req, String body, Integer code, String status, Hashtable<String,String> headers) {
		reply(req, httpResponse(body, code, status, headers));

	}

	public void deliver(String uuid, String[] idents, String data) {
		StringBuilder sb = new StringBuilder();
		for (String ident : idents) {
			if (sb.length() != 0) sb.append(" ");
			sb.append(ident);	
		}
		send(uuid, sb.toString(), data);
	}

	//	
	//	public void deliverJSON(String uuid, String[] idents, JSONObject data) {
	//		deliver(uuid, idents, data.toString());
	//
	//	}

	public void deliverHTTP(String uuid, String[] idents, String data) {
		//deliver(uuid, idents, http_response(body, code, status, headers))
	}

	public void close(Request req) {
		reply(req, "");

	}

	public void deliverClose(String uuid, String[] idents) {
		deliver(uuid, idents, "");
	}

}
