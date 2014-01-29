package messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author Tamim
 */
public class MessageManager
{
    public static String getHandshakeMessage(String peerID, String hashInfo, int numberOfPeices)
    {
        String s = String.format("p2pvideo,%s,%s,%d", peerID, hashInfo, numberOfPeices);
        int length = s.getBytes().length + 1;
        byte lengthField[] = new byte[1];
        lengthField[0] = (byte) length;
        String l = new String(lengthField);
        return String.format("%s%s", l, s);
    }
    
    /**
     * First four byte will contain the message ID, second 4 byte will contain the piece ID 
     * @param input
     * @return 
     */
    public static byte [] getVideoPieceMessage(int id, byte [] input)
    {
        byte [] output = new byte [input.length + 8];
        byte [] messgeID = ByteBuffer.allocate(4).putInt(id).array();
        byte [] pieceID =  ByteBuffer.allocate(4).putInt(2).array();

        System.arraycopy(messgeID, 0, output, 0, messgeID.length);
        System.arraycopy(pieceID, 0, output, messgeID.length, pieceID.length);
        System.arraycopy(input, 0, output, messgeID.length + pieceID.length, input.length);
        
        return output;
    }
    
    

    public static String getPIECEMessage(int chunkID, String chunk)
    {
        byte chunkArr[] = chunk.getBytes();
        byte lenthArr[] = ByteBuffer.allocate(4).putInt(chunkArr.length + 5).array();
        byte messageID[] =
        {
            7
        };
        byte chunkIDArr[] = ByteBuffer.allocate(4).putInt(chunkID).array();
        byte arr[] = new byte[chunkArr.length + 9];

        System.arraycopy(lenthArr, 0, arr, 0, lenthArr.length);
        System.arraycopy(messageID, 0, arr, lenthArr.length, messageID.length);
        System.arraycopy(chunkIDArr, 0, arr, lenthArr.length + messageID.length, chunkIDArr.length);
        System.arraycopy(chunkArr, 0, arr, lenthArr.length + messageID.length + chunkIDArr.length, chunkArr.length);

        return new String(arr);
    }

    public static ArrayList<String> parsePIECEMessage(String message)
    {
        byte msg[] = message.getBytes();
        byte chunkID[] = new byte[4];
        byte chunk[] = new byte[msg.length - 9];
        System.arraycopy(msg, 5, chunkID, 0, 4);
        System.arraycopy(msg, 9, chunk, 0, chunk.length);
        ArrayList<String> list = new ArrayList<String>();
        list.add("" + ByteBuffer.wrap(chunkID).getInt());
        list.add(new String(chunk));
        return list;
    }

    public static MessageType getType(String msg)
    {
        if (msg.getBytes()[0] == msg.length())
        {
            return MessageType.HANDSHAKE;
        }
        else
        {
            return MessageType.PIECE;
        }
    }
    
    public static MessageType getType(byte [] msg)
    {
        
        if (msg[0] == msg.length)
        {
            return MessageType.HANDSHAKE;
        }
        else
        {
            return MessageType.VIDEO_PIECE;
        }
    }    
}
