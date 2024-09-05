/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Sep 1, 2024
 */
package org.bcms.ecsrmsrp.alfresco;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.alfresco.core.handler.NodesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class GetNodeContent {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired NodesApi nodesApi;
	
	/**
     * Get a file node content bytes (folders does not have content).
     *
     * @param nodeId   the id of the file node that we want to fetch content for.
     * @return Node content info object
	 * @throws java.io.IOException 
     */
    public Resource getNodeContent(String nodeId) throws IOException, java.io.IOException {
        // Relevant when using API call from web browser, true is the default
        Boolean attachment = true;
        // Only download if modified since this time, optional
        OffsetDateTime ifModifiedSince = null;
        // The Range header indicates the part of a document that the server should return.
        // Single part request supported, for example: bytes=1-10., optional
        String range = null;

        Resource result = nodesApi.getNodeContent(nodeId, attachment, ifModifiedSince, range).getBody();
        logger.info("Got node {} size: {}", result.getFilename(), result.contentLength());

        return result;
    }
}
