package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public Label getLabel(long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label createLabel(LabelDto labelDto) {
        final Label newLabel = new Label();

        newLabel.setName(labelDto.getName());

        return labelRepository.save(newLabel);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        final Label labelToUpdate = labelRepository.findById(id).orElseThrow();

        labelToUpdate.setName(labelDto.getName());

        return labelRepository.save(labelToUpdate);
    }

    @Override
    public void deleteLabel(long id) {
        final Label labelToDelete = labelRepository.findById(id).orElseThrow();

        labelRepository.delete(labelToDelete);
    }
}
