package sample;

public class Response {
    Object data;

    String message;

    boolean ok;

    public Response(boolean ok, Object data, String message) {
        this.ok = ok;
        this.data = data;
        this.message = message;
    }

    public Response() {
        this.ok = false;
        this.data = null;
        this.message = "";
    }
}
