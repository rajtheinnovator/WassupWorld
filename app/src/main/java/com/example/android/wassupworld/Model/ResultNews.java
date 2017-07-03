package com.example.android.wassupworld.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 7/2/2017.
 */

public class ResultNews {
    @SerializedName("articles")
    private List<News> articles;
    @SerializedName("status")
    private String status;
    @SerializedName("source")
    private String source;
    @SerializedName("sortBy")
    private String sortBy;

    public ResultNews(List<News> articles, String status, String source, String sortBy) {
        this.articles = articles;
        this.status = status;
        this.source = source;
        this.sortBy = sortBy;
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
