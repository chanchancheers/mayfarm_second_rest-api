package com.mayfarm.rest_api.controller;


import com.mayfarm.rest_api.entity.OrderKey;
import com.mayfarm.rest_api.entity.Post;
import com.mayfarm.rest_api.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs
public class PostControllerTest {

//    @Autowired
//    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    PostService postService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }
    @Test
    void allPosts() throws Exception {
                mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts")
                .accept(MediaType.APPLICATION_JSON))
                        .andDo(document("findAllPosts",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("pagination.totalPosts").type(JsonFieldType.NUMBER).description("The Number of total Posts"),
                                        fieldWithPath("post.[].id").type(JsonFieldType.NUMBER).description("The ID of the Post"),
                                        fieldWithPath("post.[].subject").type(JsonFieldType.STRING).description("The Subject of the Post"),
                                        fieldWithPath("post.[].content").type(JsonFieldType.STRING).description("The Content of the Post"),
                                        fieldWithPath("post.[].createdDate").type("LocalDateTime").description("The Date of Post Created"),
                                        fieldWithPath("post.[].modifiedDate").type("LocalDateTime").description("The Date of Post modified"),
                                        fieldWithPath("status.path").type(JsonFieldType.STRING).description("The requested URL"),
                                        fieldWithPath("status.code").type(JsonFieldType.NUMBER).description("HTTP Status Code"),
                                        fieldWithPath("status.method").type(JsonFieldType.STRING).description("HTTP Method requested"),
                                        fieldWithPath("status.httpStatus").type(JsonFieldType.STRING).description("HTTP Status")
                                )
                                ));

    }
    @Test
    void allPostBy() throws Exception{
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts?order_key={orderKey}", "dated")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("findAllByOrderKey",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("order_key").description("Keyword for Ordering posts." +
                                        "\n[\"idd\" : by ID in Descending order," +
                                        "\n \"date\" : by Created date, \"dated\" : in Descending order," +
                                        "\n \"mdate\" : by Modified date, \"mdated\" : in Desending order] (Default : by Id)")
                        )
//                        queryParameters(
//                                parameterWithName("order_key").description("Keyword for Ordering posts." +
//                                        "\n[\"id\" : by Id, \"idd\" : by ID in Descending order," +
//                                        "\n \"date\" : by Created date, \"dated\" : in Descending order," +
//                                        "\n \"mdate\" : by Modified date, \"mdated\" : in Desending order]")
//                        )
                        ));
    }

    @Test
    void findPost() throws Exception {
       this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/{id}", 6)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andDo(document("findPostById",
                       pathParameters(
                               parameterWithName("id").description("Post ID you want to read.")
                       ),
                       responseFields(
                               fieldWithPath("post.[].id").type("int").description("The ID of the Post"),
                               fieldWithPath("post.[].subject").type(JsonFieldType.STRING).description("The Subject of the Post"),
                               fieldWithPath("post.[].content").type(JsonFieldType.STRING).description("The Content of the Post"),
                               fieldWithPath("post.[].createdDate").type("LocalDateTime").description("The Date of Post Created"),
                               fieldWithPath("post.[].modifiedDate").type("LocalDateTime").description("The Date of Post modified"),
                               fieldWithPath("status.path").type(JsonFieldType.STRING).description("The requested URL"),
                               fieldWithPath("status.code").type(JsonFieldType.NUMBER).description("HTTP Status Code"),
                               fieldWithPath("status.method").type(JsonFieldType.STRING).description("HTTP Method requested"),
                               fieldWithPath("status.httpStatus").type(JsonFieldType.STRING).description("HTTP Status")

                       )
                       ));
    }

    @Test
    @Transactional
    void createPost() throws Exception {

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/posts")
                .accept(MediaType.APPLICATION_JSON)
                    .content("{\"subject\" : \"Enter the subject of the post\", \n \"content\" : \"Fill the content\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("createPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("subject").type(String.class).description("The Subject of the Post, @NotBlank, Max-Size : 50"),
                                fieldWithPath("content").type(String.class).description("The Content of the Post, @NotBlank")
                        )
                ));
    }

    @Test
    @Transactional
    void modifyPost() throws Exception{
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/api/posts/{id}", 6)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"subject\" : \"Modify subject\", \"content\" : \"Modify content.\"} ")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("modifiyPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("ID of the Post you want to Edit")
                        ),

                        requestFields(
                                fieldWithPath("subject").type(String.class).description("The subject you want to edit, @NotBlank, Max-Size : 50"),
                                fieldWithPath("content").type(String.class).description("The content you want to edit, @NotBlank")
                        ))
                );
    }

    @Test
    @Transactional
    void deletePost() throws Exception{
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/posts/{id}", 5)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("deletePost",
                                pathParameters(
                                        parameterWithName("id").description("Post ID you want to DELETE.")
                                ))
                        );
    }

}