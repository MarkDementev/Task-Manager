package hexlet.code.app.service;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;

public interface LabelService {
    Label getLabel(long id);
    Iterable<Label> getLabels();
    Label createLabel(LabelDto labelDto);
    Label updateLabel(long id, LabelDto labelDto);
    void deleteLabel(long id);
}
