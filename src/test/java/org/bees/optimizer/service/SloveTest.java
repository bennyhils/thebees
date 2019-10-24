package org.bees.optimizer.service;

import org.bees.optimizer.RouteOptimizerConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RouteOptimizerConfig.class)
public class SloveTest {

    @Test
    public void sloveTest () {
        Integer moneySum  = null;
        Assert.assertEquals(moneySum, null);
    }
}
