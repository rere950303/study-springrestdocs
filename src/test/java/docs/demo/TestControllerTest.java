package docs.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static docs.demo.RestDocsSupport.field;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class TestControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RestDocumentationResultHandler restDocs;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("getAccount")
    public void getAccount() throws Exception {
        mockMvc.perform(get("/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("id").description("Account id")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("id"),
                                        fieldWithPath("name").description("name"),
                                        fieldWithPath("email").description("email")
                                ))
                );
    }

    @Test
    @DisplayName("getAccountList")
    public void getAccountList() throws Exception {
        mockMvc.perform(get("/accounts").param("size", "10").param("page", "0"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestParameters(
                                        parameterWithName("size").optional().description("size"),
                                        parameterWithName("page").optional().description("page")
                                ),
                                responseFields(
                                        fieldWithPath("data").description("데이터"),
                                        fieldWithPath("data.list[0].id").description("id"),
                                        fieldWithPath("data.list[0].email").description("email"),
                                        fieldWithPath("data.list[0].name").description("name")
                                )
                        )
                );
    }

    @Test
    @DisplayName("createAccount")
    public void createAccount() throws Exception {
        AccountDTO dto = AccountDTO.builder().name("yhw").email("yhwjjang1995@naver.com").build();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("name").description("name").attributes(field("constraints", "길이 10 이하")),
                                        fieldWithPath("email").description("email").attributes(field("constraints", "길이 30 이하"))
                                )
                        )
                );
    }
}