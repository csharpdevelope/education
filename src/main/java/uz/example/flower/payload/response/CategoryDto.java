package uz.example.flower.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.example.flower.model.dto.FlowerResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @JsonProperty("total_page")
    private long totalPage;
    @JsonProperty("total_size")
    private int totalSize;
    private int page;
    @JsonProperty("page_size")
    private int pageSize;
    private List<FlowerResponse> product;
}
