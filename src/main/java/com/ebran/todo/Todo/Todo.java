package com.ebran.todo.Todo;

public class Todo {
    private Integer id;
    private String item;

    public Todo(Integer id, String item) {
        this.id = id;
        this.item = item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
