package br.ifsp.demo.controller.exam;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/exam")
@AllArgsConstructor
@Tag(name = "Exam controller")
public class ExamController {
}
