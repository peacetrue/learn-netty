package com.github.peacetrue.learn.io.bio;

import lombok.Setter;

import java.io.IOException;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:27
 **/
@Setter
public class BIORpc {

    private BIOClient bioClient;

    public BIORpc(BIOClient bioClient) {
        this.bioClient = bioClient;
    }

    public String invoke(String line) throws IOException {
        bioClient.getBufferedWriter().write(line + "\n");
        bioClient.getBufferedWriter().flush();
        return bioClient.getBufferedReader().readLine();
    }

}
