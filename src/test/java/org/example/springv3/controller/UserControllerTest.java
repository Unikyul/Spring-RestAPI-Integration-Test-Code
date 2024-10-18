package org.example.springv3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.user.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//아래 2 어노테이션은 항상 고정이다.

//Mock을 빈 컨테이너에 띄우는 것.
@AutoConfigureMockMvc
//MOCK 환경은 가짜 8080포트를 띄우는 것이다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper om = new ObjectMapper();


    @Test
    public void join_test() throws Exception {
        //given
        UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
        joinDTO.setUsername("haha");
        joinDTO.setPassword("1234");
        joinDTO.setEmail("haha@nate.com");

        //JoinDTO를 json으로 바꾸는 것. 즉 asString으로 읽는 것
        String requestBody = om.writeValueAsString(joinDTO);
        //System.out.println(requestBody);


        //mockMvc.perform을 해서 특정 주소에 통신을 수행해보는 것이다.
        //주소는 get, post 등을 정하고 post라면 .content가 필요 (우리가 만들어둔 json을 넣어줌)
        //.contentType으로 타입을 정해서 보내준다.
        //그 결과를 actions로 받는 것이다.
        //이때 join 엔드포인트로 POST 요청을 하는 것인데 이는 MockMvcRequestBuilders.post() 메서드를 사용하여 구현하므로
        //MockMvcRequestBuilders를 import 해주면  그냥 mockMvc((post())  이런식으로 사용할 수 있다.  get, post, put 등 사용가능
        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //eyes
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("200"));


    }


}
