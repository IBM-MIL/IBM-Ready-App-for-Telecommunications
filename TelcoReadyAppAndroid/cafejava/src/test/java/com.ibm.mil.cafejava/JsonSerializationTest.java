/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.cafejava;

import com.google.gson.Gson;
import com.worklight.wlclient.api.WLResponse;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rx.Observable;
import rx.functions.Action1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class JsonSerializationTest {
    private Observable<WLResponse> mockObservable;

    @Before
    public void setUp() {
        WLResponse wlResponse = Mockito.mock(WLResponse.class);
        Mockito.when(wlResponse.getResponseJSON())
                .then(new Answer<JSONObject>() {
                    @Override
                    public JSONObject answer(InvocationOnMock invocation) throws Throwable {
                        Person person = new Person();
                        person.name = "John";
                        person.age = 25;
                        person.isDeveloper = true;
                        return new JSONObject(new Gson().toJson(person));
                    }
                });
        mockObservable = Observable.just(wlResponse);
    }

    @Test
    public void testClassSerial() {
        mockObservable
                .compose(CafeJava.serializeTo(Person.class))
                .subscribe(new Action1<Person>() {
                    @Override public void call(Person person) {
                        assertNotNull("Person is null after serialization", person);
                        assertEquals(person.name, "John");
                        assertEquals(person.age, 25);
                        assertEquals(person.isDeveloper, true);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        fail("Exception thrown: " + throwable.getMessage());
                    }
                });
    }

    static class Person {
        String name;
        int age;
        boolean isDeveloper;
    }

}
