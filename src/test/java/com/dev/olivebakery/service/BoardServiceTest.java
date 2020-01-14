package com.dev.olivebakery.service;

import com.dev.olivebakery.repository.BoardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardServiceTest {

    @MockBean
    BoardRepository boardRepository;

    @Before
    public void startBoardServiceTest(){

    }

    @Test
    public void getPosts() {
    }

    @Test
    public void getNoticePosts() {
    }

    @Test
    public void getPost() {
    }
}