
package org.gvsig.remoteClient;

import org.gvsig.remoteClient.wms.ICancellable;

/**
 * <p></p>
 *
 */
public abstract class RemoteClient {

/**
 * <p>Represents ...</p>
 *
 */
    protected String hostName;

/**
 * <p>Represents ...</p>
 *
 */
    protected int port;

/**
 * <p>Represents ...</p>
 *
 */
    protected String serviceName;

/**
 * <p>Represents ...</p>
 *
 */
    private String type;

/**
 * <p>Represents ...</p>
 *
 */
    private String subtype;

/**
 * <p>Represents ...</p>
 *
 *
 * @return
 */
    public String getHost() {
        return hostName;
    }

/**
 * <p>Represents ...</p>
 *
 *
 * @param _hostName
 */
    public void setHost(String _hostName) {
        hostName = _hostName;
    }

/**
 * <p>Represents ...</p>
 *
 *
 * @return
 */
    public int getPort() {
        // your code here
        return port;
    }

/**
 * <p>Does ...</p>
 *
 *
 * @param _port
 */
    public void setPort(int _port) {
        port = _port;
    }

/**
 * <p>Does ...</p>
 *
 *
 * @return
 */
    public String getServiceName() {
        // your code here
        return serviceName;
    }

/**
 * <p>Does ...</p>
 *
 *
 * @param _serviceName
 */
    public void setServiceName(String _serviceName) {
        serviceName = _serviceName;
    }

/**
 * <p>Does ...</p>
 *
 */
    public abstract boolean connect(boolean override, ICancellable cancel);

/**
 * <p>Does ...</p>
 *
 */
    public abstract void close();

/**
 * <p>Represents ...</p>
 *
 *
 * @return
 */
    public String getType() {
        return type;
    }

/**
 * <p>Represents ...</p>
 *
 *
 * @param _type
 */
    public void setType(String _type) {
        type = _type;
    }

/**
 * <p>Represents ...</p>
 *
 *
 * @return
 */
    public String getSubtype() {
        return subtype;
    }

/**
 * <p>Represents ...</p>
 *
 *
 * @param _subtype
 */
    public void setSubtype(String _subtype) {
        subtype = _subtype;
    }
 }
