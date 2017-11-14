package com.oole.hw3;

import com.oole.hw3.utility.PropertiesUtils;
import org.junit.Assert;
import org.junit.Test;

public class PropertiesUtilsTest {

    @Test
    public void testSetProperty(){
        PropertiesUtils.setProperty("username","terminator");
        Assert.assertEquals(PropertiesUtils.getProperties().getProperty("username"), "terminator");

    }

}
