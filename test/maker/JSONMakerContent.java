package maker;

//import jsonacious.JSONMaker;

import java.util.Map;

public class JSONMakerContent
//    extends JSONMaker
{
//    @Override
    public void put( Object target, String key, Object value )
    {
        Content content = (Content) target;
        switch( key ) {
            case "uri":
                content.uri = (String) value;
                break;
            case "height":
                content.height = (Integer) value;
                break;
            case "title":
                content.title = (String) value;
                break;
            case "width":
                content.width = (Integer) value;
                break;
            case "format":
                content.format = (String) value;
                break;
            case "duration":
                content.duration = (Integer) value;
                break;
            case "size":
                content.size = (Integer) value;
                break;
            case "bitrate":
                content.bitrate = (Integer) value;
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
