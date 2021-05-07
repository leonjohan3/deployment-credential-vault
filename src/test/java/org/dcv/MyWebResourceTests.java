package org.dcv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

//@ExtendWith(MockitoExtension.class)
public class MyWebResourceTests {

//    @Mock
//    private MyWebResource myWebResource;

    @Test
    public void should() {

        // given: all data (test fixture) preparation
        final MyWebResource myWebResource = new MyWebResource();
        final String name = "name";

        // when : method to be checked invocation
        final String result = myWebResource.hello(name);

        // then : checks and assertions
        assertThat(result, is("hello " + name));
    }
}
