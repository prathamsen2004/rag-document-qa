package com.pratham.rag_document_qa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Embedding {

    private List<Float> values;

}