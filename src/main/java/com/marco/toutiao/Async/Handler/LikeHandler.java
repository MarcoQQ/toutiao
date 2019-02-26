package com.marco.toutiao.Async.Handler;

import com.marco.toutiao.Async.EventHandler;
import com.marco.toutiao.Async.EventModel;
import com.marco.toutiao.Async.EventType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Override
    public void doHandle(EventModel model) {
        System.out.println("Liked");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
