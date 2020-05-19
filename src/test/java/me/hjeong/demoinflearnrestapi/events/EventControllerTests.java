
package me.hjeong.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hjeong.demoinflearnrestapi.common.RestDocsConfiguration;
import me.hjeong.demoinflearnrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 9, 1, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 10, 31, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 0))
                .endEventDateTime(LocalDateTime.of(2018,11,24,14,0))
                .basePrice(0)
                .maxPrice(0)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("offline").value(false))
                .andExpect(jsonPath("free").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(true)))
                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-events",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("...")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("입력받을 수 없는 값을 사용해서 이벤트를 생성하는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 9, 1, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 10, 31, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 0))
                .endEventDateTime(LocalDateTime.of(2018,11,24,14,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .offline(true)
                .free(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우 이벤트를 생성하는 경우에 에러가 발생하는 테스트")
    public void createEvent_Empty_Request() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        String json = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우 이벤트를 생성하는 경우에 에러가 발생하는 테스트")
    public void createEvent_Wrong_Request() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 9, 1, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 10, 31, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 11, 23, 14, 0))
                .endEventDateTime(LocalDateTime.of(2018,11,24,14,0))
                .basePrice(500)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        String json = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
    @Test
    @TestDescription("Bad Request 본문에 에러 표시하기")
    public void createEvent_Bad_Request_Body() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 9, 1, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 10, 31, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 11, 23, 14, 0))
                .endEventDateTime(LocalDateTime.of(2018,11,24,14,0))
                .basePrice(500)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        String json = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
}
