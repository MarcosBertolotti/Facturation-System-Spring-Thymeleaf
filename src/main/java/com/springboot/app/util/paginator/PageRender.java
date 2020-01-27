package com.springboot.app.util.paginator;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRender<T> {

    private String url;
    private Page<T> page;

    private int totalPage;
    private int numElementXPage;
    private int actualPage;

    private List<PageItem> pages;

    public PageRender(String url, Page<T> page){
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<PageItem>();

        numElementXPage = page.getSize();
        totalPage = page.getTotalPages();
        actualPage = page.getNumber() + 1;

        int from, to;

        if(totalPage <= numElementXPage){
            from = 1;
            to = totalPage;
        }else{
            if(actualPage <= numElementXPage/2){
                from = 1;
                to = numElementXPage;
            }else if(actualPage >= totalPage - numElementXPage/2){
                from = totalPage - numElementXPage + 1;
                to = numElementXPage;
            }else{
                from = actualPage - numElementXPage/2;
                to = numElementXPage;
            }
        }

        for(int i=0; i < to; i++){
            pages.add(new PageItem(from + i, actualPage == from + i));
        }
    }

    public boolean isFirst(){
        return page.isFirst();
    }

    public boolean isLast(){
        return page.isLast();
    }

    public boolean isHasNext(){
        return page.hasNext();
    }

    public boolean isHasPrevious(){
        return page.hasPrevious();
    }

}
