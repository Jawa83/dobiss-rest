package com.jawa83.domotica.dobiss.core.domotica;

import com.jawa83.domotica.dobiss.core.domotica.data.DobissInput;
import com.jawa83.domotica.dobiss.core.domotica.model.AbstractDomoticaAddress;
import com.jawa83.domotica.dobiss.core.domotica.model.Dimmer;
import com.jawa83.domotica.dobiss.core.domotica.model.Group;
import com.jawa83.domotica.dobiss.core.domotica.model.Mood;
import com.jawa83.domotica.dobiss.core.domotica.model.Toggle;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create connection with Dobiss domotics Lan interface
 */
@Slf4j
@Component
public class Connection {

    private final static int DELAY_BEFORE_NEXT_ATTEMPT = 25;
    private final static int NUMBER_OF_ATTEMPTS = 25;
    private final static int DEFAULT_MAX_LINES = 100;

    public static final int MOODS_ID = -1;

    private final static DobissInput[] MODULES = {
            DobissInput.FETCH_OUTPUTS_1,
            DobissInput.FETCH_OUTPUTS_2,
            DobissInput.FETCH_OUTPUTS_3,
            DobissInput.FETCH_OUTPUTS_4,
            DobissInput.FETCH_OUTPUTS_5,
            DobissInput.FETCH_OUTPUTS_6,
            DobissInput.FETCH_OUTPUTS_7,
            DobissInput.FETCH_OUTPUTS_8,
            DobissInput.FETCH_OUTPUTS_9,
            DobissInput.FETCH_OUTPUTS_10
    };
    private final static int NUMBER_OF_MODULES = MODULES.length;

    private final static DobissInput[] STATUS = {
            DobissInput.STATUS_OUTPUTS_1,
            DobissInput.STATUS_OUTPUTS_2,
            DobissInput.STATUS_OUTPUTS_3,
            DobissInput.STATUS_OUTPUTS_4,
            DobissInput.STATUS_OUTPUTS_5,
            DobissInput.STATUS_OUTPUTS_6,
            DobissInput.STATUS_OUTPUTS_7,
            DobissInput.STATUS_OUTPUTS_8,
            DobissInput.STATUS_OUTPUTS_9,
            DobissInput.STATUS_OUTPUTS_10
    };

    private List<Group> groupList = new ArrayList<>();

    private final String ip;
    private final int port;

    private Socket socket;

    public Connection(){

        /*
        TODO load ip and port from preferences
        }
        */

        ip = "192.168.0.197";
        port = 1001;
    }

    // Import all data in memory at once, avoid multiple tcp connections
    @PostConstruct
    public boolean importData(){
        boolean success = loadGroups();
        success &= loadOutputs();
        success &= loadMoods();
        success &= closeConnection();

        return success;
    }

    public List<Group> getGroups() {
        if(groupList == null || groupList.size() == 0) {
            loadGroups();
            loadMoods();
            closeConnection();
        }
        return groupList;
    }

    private boolean loadGroups() {

        try {
            groupList = new ArrayList<>();
            byte[] receive = new Network().execute(
                    DobissInput.FETCH_GROUPS.getRequest(),
                    ConversionUtils.intToBytes(DobissInput.FETCH_GROUPS.getMaxLines()));
            // TODO check result error
            String groupsl= new String(receive);
            groupsl = groupsl.substring(0,groupsl.length() - 16);

            for(int i = 0; i < groupsl.length()/32; i++){
                groupList.add(new Group(i, groupsl.substring(i*32, (i+1)*32).trim()));
            }

            log.info("Groups: Imported: " + groupList.toString());
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private boolean loadOutputs(){
        try {
            byte[] receive;
            for(int i = 0; i < NUMBER_OF_MODULES; i++){
                receive = new Network().execute(MODULES[i].getRequest());
                for (int j = 0; j < receive.length / 32; j++){
                    byte[] temp = Arrays.copyOfRange(receive,j*32,(j+1)*32);
                    byte index = temp[temp.length-1];
                    String name = new String(temp).substring(0,30).trim();

                    Group group = getGroupById(index);
                    if (group == null) {
                        log.warn("Outputs: No group found for " + name);
                    } else {
                        log.debug("Outputs: " + name + " belongs to group " + group.getName());
                        if (name.contains("spot")) { // TODO investigate a better way to determine whether the 'output' is a 'Dimmer'
                            log.debug("Outputs: " + name + " is a Dimmer");
                            group.getItems().add(new Dimmer(name, j, i+1));
                        } else {
                            log.debug("Outputs: " + name + " is a Toggle");
                            group.getItems().add(new Toggle(name, j, i + 1));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadMoods() {

        try {
            byte[] receive = new Network().execute(DobissInput.FETCH_MOODS.getRequest());
            // TODO check error
            Group moodGroup = new Group(MOODS_ID, "sferen");

            String moodsl = new String(receive);
            moodsl = moodsl.substring(0, moodsl.length() - 16);

            for (int i = 0; i < moodsl.length() / 32; i++) {
                Mood mood = new Mood(moodsl.substring(i * 32, (i + 1) * 32).trim(), i);
                moodGroup.getItems().add(mood);
            }

            log.info("Moods: Imported: " + moodGroup.toString());
            groupList.add(moodGroup);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean closeConnection(){
        try{
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleOutput(Toggle output){
        try {
            new Network().execute(output.getToggleRequest());
            log.info("Toggle: " + output.getName());
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
        return true;
    }
    
    public boolean dimOutput(Dimmer output, int value){
        try {
            new Network().execute(output.getDimmerRequest(value));
            log.info("Dimmer(" + value + "): " + output.getName());
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
        return true;
    }

    public byte[][] getStatusOfGroup(Group group){
        byte[][] receive = new byte[NUMBER_OF_MODULES][];

        try {
            // Check which modules are actually used by outputs in this group
            // Only fetch the status for those modules
            List<DobissInput> modulesInGroup = new ArrayList<>();
            DobissInput moduleRequest;
            for (AbstractDomoticaAddress item : group.getItems()) {
                moduleRequest = STATUS[item.getModule() - 1];
                if (!modulesInGroup.contains(moduleRequest)) {
                    modulesInGroup.add(moduleRequest);
                }
            }

            int index = 0;
            for (DobissInput input : STATUS) {
                if (modulesInGroup.contains(input)) {
                    byte[] rec = new Network().execute(input.getRequest(), ConversionUtils.intToBytes(input.getMaxLines()));
                    byte[] result = rec;
                    if (result != null) {
                        // trim unused addresses
                        int lastIndex = -1;
                        for (int j = 0; j < result.length; j++) {
                            if (result[j] == (byte) 255) {
                                lastIndex = j;
                                break;
                            }
                        }
                        if (rec.length > 0) {
                            receive[index] = Arrays.copyOf(result, lastIndex);
                            log.info("Status of module " + index + ": " + ConversionUtils.bytesToHex(receive[index]));
                        }
                    } else {
                        log.warn("Status of module " + index + ": Not found!");
                    }
                }
                index++;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
        return receive;
    }

    public byte[] getStatusOfModule(int module) throws Exception {
        // TODO construct request instead of getting it from STATUS
        byte[] rec = new Network().execute(STATUS[module-1].getRequest(), ConversionUtils.intToBytes(4));
        if (rec != null) {
            // trim unused addresses
            int lastIndex = -1;
            for (int j = 0; j < rec.length; j++) {
                if (rec[j] == (byte) 255) {
                    lastIndex = j;
                    break;
                }
            }
            if (rec.length > 0) {
                log.info("Status of module " + module + ": " + ConversionUtils.bytesToHex(Arrays.copyOf(rec, lastIndex)));
                return Arrays.copyOf(rec, lastIndex);
            }
        } else {
            log.warn("Status of module " + module + ": Not found!");
        }
        return null;
    }

    public byte[][] getStatusOfAllModules() throws Exception {
        byte[][] result = new byte[NUMBER_OF_MODULES][];
        for (int i = 1; i < NUMBER_OF_MODULES; i++) {
            result[i] = getStatusOfModule(i);
        }
        return result;
    }

    private Group getGroupById(int id) {
        for (Group group : groupList) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    void sendRequest(String request) {
    		try {
				new Network().execute(ConversionUtils.hexToBytes(request));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    private class Network {

        protected byte[] execute(byte[]... params) throws Exception {

            try {
                int maxLines = params.length > 1 ? ConversionUtils.bytesToInt(params[1]) : DEFAULT_MAX_LINES;

                InetAddress address = InetAddress.getByName(ip);

                if (socket == null || socket.isClosed() || !socket.isConnected()) {
                    log.info("Opening new socket");
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(address, port), 3000);
                }

                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                dos.write(params[0]);
                log.debug("Socket Request: " + ConversionUtils.bytesToHex(params[0]));

                InputStream in = socket.getInputStream();
                DataInputStream dis = new DataInputStream(in);

                byte[] data = new byte[maxLines * 16];

                int count = 0;
                while (count < maxLines) {
                    byte[] temp = new byte[16];
                    int attempt = 0;
                    while (dis.available() == 0 && attempt < NUMBER_OF_ATTEMPTS) {
                        // response might be a bit later
                        // wait a bit before checking again
                        Thread.sleep(DELAY_BEFORE_NEXT_ATTEMPT);
                        attempt++;
                    }
                    if (dis.available() != 0) {
                        if (dis.read(temp, 0, 16) > 0) {
                            log.debug("Socket response: " + ConversionUtils.bytesToHex(temp));
                            for (int j = 0; j < temp.length; j++) {
                                data[count * 16 + j] = temp[j];
                            }
                        }
                        count++;
                    } else {
                        break;
                    }
                }

                if (count == 0) {
                    log.warn("Connection: No response for request " + ConversionUtils.bytesToHex(params[0]));
                    // TODO make custom Exception class
                    throw new Exception("No response for request " + ConversionUtils.bytesToHex(params[0]));
                }
                return Arrays.copyOfRange(data, 32, count * 16);

            } catch (SocketTimeoutException e) {
                log.warn("Connection: Timeout for request " + ConversionUtils.bytesToHex(params[0]));
                throw e;
            }catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

    }
}
