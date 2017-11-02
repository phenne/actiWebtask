package rpc.argresolver;

import bd.Transaction;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoArgResolverTest {

    @Test
    public void resolveArg() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        Transaction transaction = mock(Transaction.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("transaction")).thenReturn(transaction);
    }
}
