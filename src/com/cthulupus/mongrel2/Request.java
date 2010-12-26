package com.cthulupus.mongrel2;


public class Request {

	public boolean data;
	public String body;
	public String connID;
	public String sender;

	static Integer UUIDMODE = 0;
	static Integer IDMODE = 1;
	static Integer PATHMODE = 2;
	static Integer LENGTHMODE = 3;
	
	private String uuid = null;
	private Integer id = null;
	private String path = null;
	private Integer length = null;
	private byte[] rest;
	
	public Request(String uuid, Integer id, String path, Integer length, byte[] rest) {
		this.setUuid(uuid);
		this.setId(id);
		this.setPath(path);
		this.setLength(length);
		this.rest = rest;
	}

	public static Request parse(byte[] recv) {
		String uuid = null;
		Integer id = null;
		String path = null;
		Integer length = null;
			StringBuilder sb = new StringBuilder();
			Integer mode = 0;
			byte[] rest = null;
			for (int i=0; i < recv.length; i++) {
				if (recv[i] != ' ' && recv[i] != ':') {
					sb.append((char)recv[i]);
				} else {
					if (mode == UUIDMODE) {
						uuid = sb.toString();
					} else if (mode == IDMODE) {
						id = Integer.parseInt(sb.toString());
					} else if (mode == PATHMODE) {
						path = sb.toString();
					} else if (mode == LENGTHMODE) {
						length = Integer.parseInt(sb.toString());

						rest = new byte[recv.length - i - 1];
						System.arraycopy( recv, i + 1, rest, 0, rest.length);
						break;

					}
					mode++;
					sb = new StringBuilder();
				}
			}
			
			return new Request(uuid,id,path,length, rest);	
	}

	public byte[] getRest() {
		return rest;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getLength() {
		return length;
	}
}
