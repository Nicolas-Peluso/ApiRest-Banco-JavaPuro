package com.nicolas;
import java.io.IOException;

import com.nicolas.HttpRequests_.HttpReq;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        HttpReq e = new HttpReq();
        e.StartServer();
    }
}
