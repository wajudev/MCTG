package com.example.mctg.rest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestContextTest {
    @Mock
    Socket socketMock;

    @SneakyThrows
    @Test
    @DisplayName("Handle a Socket within RequestContext")
    void testSocketHandler(){
        String request = "POST /messages HTTP/1.1\n" +
                "HOST: localhost\n" +
                "Content-Type: text/plain; charset=UTF-8\n" +
                "\n" +
                "Hallo Welt";

        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(request.getBytes()));
        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream(64));

        // act
        new RequestContext(socketMock);

        // assert
        //verify(socketMock).getInputStream();
    }
}