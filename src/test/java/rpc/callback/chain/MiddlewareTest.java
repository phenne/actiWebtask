package rpc.callback.chain;

import org.junit.Test;
import rpc.error.RpcException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MiddlewareTest {

    private Middleware middleware1 = new Middleware() {
        @Override
        public boolean check(HttpServletRequest request, Method method) throws RpcException {
            return true;
        }
    };

    private Middleware middleware2 = new Middleware() {
        @Override
        public boolean check(HttpServletRequest request, Method method) throws RpcException {
            return false;
        }
    };

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private Method method = mock(Method.class);

    @Test
    public void linkWith() {
        middleware1.linkWith(middleware2);

        assertEquals(middleware2, middleware1.getNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void linkWith_selfLink() {
        middleware1.linkWith(middleware1);
    }

    @Test
    public void linkWith_linkNull() {
        middleware1.linkWith(null);
        assertNull(middleware1.getNext());
    }

    @Test
    public void checkNext() throws Exception {
        middleware1.linkWith(middleware2);
        assertFalse(middleware1.checkNext(request, method));
    }

    @Test
    public void checkNext_nextNull() throws Exception {
        assertTrue(middleware1.checkNext(request, method));
    }
}
