package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {
    Label getLabel(long id);
    List<Label> getLabels();
    Label createLabel(LabelDto labelDto);
    Label updateLabel(long id, LabelDto labelDto);
    void deleteLabel(long id);
}
