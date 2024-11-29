package com.blackcompany.eeos.comment.application.model;

import com.blackcompany.eeos.comment.application.exception.ExceedContentLimitLengthException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentModelTest {

    @Test
    @DisplayName("내용 길이가 500자 이상일 경우 예외가 발생하지 않는다.")
    void createTest(){
        // given
        String length_501_word = "나는 심리학에서 기억에 대해 배우며 가장 흥미로웠던 점은 기억이 단순히 정보를 저장하는 역할에 그치지 않고, 우리가 현재 행동하고 생각하는 데 큰 영향을 미친다는 것이다. 특히, 단기 기억과 작업 기억의 차이를 이해하면서 우리의 뇌가 정보를 얼마나 효율적으로 처리하는지 놀라웠다. 단기 기억은 정보를 잠시 동안 유지하는 데 초점이 맞춰져 있지만, 작업 기억은 저장된 정보를 조작하고 활용하여 문제를 해결하거나 새로운 아이디어를 떠올리는 데 중요한 역할을 한다는 것을 알게 되었다. 나는 이 사실을 내 가장 좋아하는 사람인 [이름]에게 이야기하며, 작업 기억의 예로 숫자를 단순히 외우는 것과 계산 문제를 풀 때 머릿속에서 정보를 조합하는 차이를 설명했다. 그는 이 내용을 듣고 “우리가 일상적으로 쓰는 작업 기억이 이렇게 중요한 줄 몰랐다. 앞으로 공부하거나 일할 때 더 효율적으로 활용해야겠다.”라며 흥미를 보였다. 이 대화를 통해 기억에 대한 이해가 깊어졌다.";
        CommentModel comment = CommentModel.builder()
                .content(length_501_word)
                .build();

        // when & then
        Assertions.assertDoesNotThrow(comment::validateCreate);

    }



}
