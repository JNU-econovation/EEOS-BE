package com.blackcompany.eeos.common.util;

import com.blackcompany.eeos.common.utils.RequestScope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RequestScopeTest {

    @Test
    @DisplayName("저장한_값을_꺼낼_수_있다")
    public void requestScope() {
        RequestScope.setMemberId(1L);
        Assertions.assertEquals(1L, RequestScope.getMemberId());
    }

    @Test
    @DisplayName("서로_다른_스레드에서_저장한_값은_접근할_수_없다")
    public void clear() {
        Thread setter = new Thread(() -> {
            RequestScope.setMemberId(1L);
            Assertions.assertEquals(1L, RequestScope.getMemberId());
        });

        Thread getter = new Thread(()->{
            Assertions.assertNotEquals(1L, RequestScope.getMemberId());
        });
    }

    @Test
    @DisplayName("null을_저장하면_null이_반환된다")
    public void nullValue1(){
        RequestScope.setMemberId(null);
        Assertions.assertNull(RequestScope.getMemberId());
    }

    @Test
    @DisplayName("아무것도_저장하지_않으면_null이_반환된다")
    public void nullValue2(){
        Assertions.assertNull(RequestScope.getMemberId());
    }

}
