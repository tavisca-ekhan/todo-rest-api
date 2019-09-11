package com.ebran.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ebran.todo.Todo.Todo;
import com.ebran.todo.Todo.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ComponentScan(basePackages = "com.ebran.todo")
public class TodoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TodoService todoService;

	@Test
	public void getItems() throws Exception {
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(1,"Wake Up!!!!!"));
		given(todoService.findAll()).willReturn(mockList);
		this.mockMvc
				.perform(get("/todo"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{'id': 1,'item': 'Wake Up!!!!!'}]"));
	}

	@Test
	public void getOneItemById() throws Exception {
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(1, "Ebran"));
		mockList.add(new Todo(2, "Khan"));
		given(todoService.findOneById(1)).willReturn(mockList.get(0));
		this.mockMvc
				.perform(get("/todo/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id': 1,'item': 'Ebran'}"));
	}

	@Test
	public void itemNotFoundForId() throws Exception {
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(1, "Ebran"));
		mockList.add(new Todo(2, "Khan"));
		given(todoService.findOneById(3)).willReturn(null);
		this.mockMvc
				.perform(get("/todo/3"))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Item not found"));
	}

	@Test
	public void postItem() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		given(todoService.addNewItem(new Todo(1,"Ebran"))).willReturn(new Todo(1,"Ebran"));
		mockMvc.perform(post("/todo")
				.contentType("application/json")
				.content(mapper.writeValueAsString(new Todo(1,"Ebran"))))
				.andExpect(status().isCreated());
	}

	@Test
	public void itemNotPosted() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		given(todoService.addNewItem(new Todo(1,""))).willReturn(new Todo(1,""));
		mockMvc.perform(post("/todo")
				.contentType("application/json")
				.content(mapper.writeValueAsString(new Todo(1,""))))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Please enter any item"));
	}

	@Test
	public void testUpdatingItem() throws Exception {
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(2, "Ebran"));
		mockList.add(new Todo(1, "Khan"));
		given(todoService.editOneById(2, "Kankana")).willReturn(new Todo(2,"Kankana"));
		this.mockMvc.perform(patch("/todo/2?item=Kankana"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id': 2,'item': 'Kankana'}"));
	}

	@Test
	public void itemNotUpdatedDueToEmptyItemBody() throws Exception {
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(2, "Ebran"));
		mockList.add(new Todo(1, "Khan"));
		given(todoService.editOneById(2, "")).willReturn(new Todo(2,""));
		this.mockMvc.perform(patch("/todo/2?item="))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Please enter any item"));
	}

	@Test
	public void itemDeletedById() throws Exception{
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(1, "Ebran"));
		given(todoService.deleteOneById(mockList.get(0).getId())).willReturn(true);
		this.mockMvc
				.perform(delete("/todo/1"))
				.andExpect(status().isOk())
				.andExpect(content().string("Item Deleted"));

	}

	@Test
	public void itemNotDeletedById() throws Exception {
		List<Todo> mockList = new ArrayList<>();
		mockList.add(new Todo(1, "Ebran"));
		given(todoService.deleteOneById(2)).willReturn(false);
		this.mockMvc
				.perform(delete("/todo/2"))
				.andExpect(status().isGone())
				.andExpect(content().string("Item not deleted"));
	}
}
