package maker;

public class ContentMaker {
    public void set( Content content, String key, Object value )
    {
        switch( key ) {
            case "uri":
                content.uri = (String) value;
                break;
            case "height":
                content.height = (int) value;
                break;
            case "title":
                content.title = (String) value;
                break;
            case "width":
                content.width = (int) value;
                break;
            case "format":
                content.format = (String) value;
                break;
            case "duration":
                content.duration = (long) value;
                break;
            case "size":
                content.size = (int) value;
                break;
            case "bitrate":
                content.bitrate = (int) value;
                break;
            case "copyright":
                content.copyright = (String) value;
                break;
            case "player":
                content.player = (String) value;
                break;
        }
    }
}
