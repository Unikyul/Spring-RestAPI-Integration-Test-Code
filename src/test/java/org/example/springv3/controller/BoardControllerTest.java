package org.example.springv3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.board.BoardRequest;
import org.example.springv3.core.util.JwtUtil;
import org.example.springv3.user.User;
import org.example.springv3.user.UserRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//@IgnoreForbiddenApisErrors() 이걸 만들어서 TEST코드에서 토큰을 만들어서 테스트한다.


@Transactional//롤백이 된다.
//아래 2 어노테이션은 항상 고정이다.

//Mock을 빈 컨테이너에 띄우는 것.
@AutoConfigureMockMvc
//MOCK 환경은 가짜 8080포트를 띄우는 것이다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper om = new ObjectMapper();
    private String accessToken;

    @BeforeEach // 메소드 실행되기 전에 실행되는게 BeforeEach,리턴을 받지 못한다. 또 다른 하나는 BeforeALl 단 한번만 실행된다!
    public void setUp() {
        System.out.println("나 실행되고 있니?");
        User sessionUser = User.builder().id(1).username("ssar").build();
        accessToken = JwtUtil.create(sessionUser); // 클래스 필드 accessToken에 할당
        System.out.println(accessToken);
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 2;

        // when
        ResultActions actions = mockMvc.perform(
                delete("/api/board/" + id)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body").isEmpty());

    }


    @Test
    public void save_test() throws Exception {
        //given
        //토큰 만들기


        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("title 11");
        saveDTO.setContent("content 11");

        //JoinDTO를 json으로 바꾸는 것. 즉 asString으로 읽는 것
        String requestBody = om.writeValueAsString(saveDTO);
        //System.out.println(requestBody);


        //mockMvc.perform을 해서 특정 주소에 통신을 수행해보는 것이다.
        //주소는 get, post 등을 정하고 post라면 .content가 필요 (우리가 만들어둔 json을 넣어줌)
        //.contentType으로 타입을 정해서 보내준다.
        //그 결과를 actions로 받는 것이다.
        //이때 join 엔드포인트로 POST 요청을 하는 것인데 이는 MockMvcRequestBuilders.post() 메서드를 사용하여 구현하므로
        //MockMvcRequestBuilders를 import 해주면  그냥 mockMvc((post())  이런식으로 사용할 수 있다.  get, post, put 등 사용가능
        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/board")
                .header("Authorization", "Bearer " + accessToken)
                //Body가 필요없으면 content를 사용 할 이유가 없다 기본중에 기본!
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //eyes
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        //then
        //모든 JSON 타입의 데이터를 받기 위해서 모든 걸 다 테스트에 적어야한다! 안 적으면 RETS API 문서를 뽑지 못한다. Ex) id,username,email등등
        //받는 모든 데이터를 다 적어서 확인해야지 REST API 문서에서 다 뽑아서 만들어준다.
//        actions.andExpect(jsonPath("$.status").value(200));
//        actions.andExpect(jsonPath("$.msg").value("성공"));
//        actions.andExpect(jsonPath("$.body.id").value(4));


    }

    @Test
    public void login_test() throws Exception {
        //given
        UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO();
        loginDTO.setUsername("ssar");
        loginDTO.setPassword("1234");


        //JoinDTO를 json으로 바꾸는 것. 즉 asString으로 읽는 것
        String requestBody = om.writeValueAsString(loginDTO);
        System.out.println(requestBody);

        //mockMvc.perform을 해서 특정 주소에 통신을 수행해보는 것이다.
        //주소는 get, post 등을 정하고 post라면 .content가 필요 (우리가 만들어둔 json을 넣어줌)
        //.contentType으로 타입을 정해서 보내준다.
        //그 결과를 actions로 받는 것이다.
        //이때 join 엔드포인트로 POST 요청을 하는 것인데 이는 MockMvcRequestBuilders.post() 메서드를 사용하여 구현하므로
        //MockMvcRequestBuilders를 import 해주면  그냥 mockMvc((post())  이런식으로 사용할 수 있다.  get, post, put 등 사용가능
        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //eyes
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        String responseJWT = actions.andReturn().getResponse().getHeader("Authorization");
        System.out.println(responseJWT);

        //then
        //모든 JSON 타입의 데이터를 받기 위해서 모든 걸 다 테스트에 적어야한다! 안 적으면 RETS API 문서를 뽑지 못한다. Ex) id,username,email등등
        //받는 모든 데이터를 다 적어서 확인해야지 REST API 문서에서 다 뽑아서 만들어준다.
        //notnull를 넣은 이유는?
        actions.andExpect(header().string("Authorization", Matchers.notNullValue()));
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body").isEmpty());


    }


    @Test
    public void save400_test() throws Exception {
        //given
        //토큰 만들기


        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("");
        saveDTO.setContent("content 11");

        //JoinDTO를 json으로 바꾸는 것. 즉 asString으로 읽는 것
        String requestBody = om.writeValueAsString(saveDTO);
        //System.out.println(requestBody);


        //mockMvc.perform을 해서 특정 주소에 통신을 수행해보는 것이다.
        //주소는 get, post 등을 정하고 post라면 .content가 필요 (우리가 만들어둔 json을 넣어줌)
        //.contentType으로 타입을 정해서 보내준다.
        //그 결과를 actions로 받는 것이다.
        //이때 join 엔드포인트로 POST 요청을 하는 것인데 이는 MockMvcRequestBuilders.post() 메서드를 사용하여 구현하므로
        //MockMvcRequestBuilders를 import 해주면  그냥 mockMvc((post())  이런식으로 사용할 수 있다.  get, post, put 등 사용가능
        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/board")
                .header("Authorization", "Bearer " + accessToken)
                //Body가 필요없으면 content를 사용 할 이유가 없다 기본중에 기본!
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //eyes
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        //then
        //모든 JSON 타입의 데이터를 받기 위해서 모든 걸 다 테스트에 적어야한다! 안 적으면 RETS API 문서를 뽑지 못한다. Ex) id,username,email등등
        //받는 모든 데이터를 다 적어서 확인해야지 REST API 문서에서 다 뽑아서 만들어준다.
//        actions.andExpect(jsonPath("$.status").value(200));
//        actions.andExpect(jsonPath("$.msg").value("성공"));
//        actions.andExpect(jsonPath("$.body.id").value(4));


    }


}
