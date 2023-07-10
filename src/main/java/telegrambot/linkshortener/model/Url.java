package telegrambot.linkshortener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JdbcType(LongVarcharJdbcType.class)
    private String longUrl;

    @NotNull
    @JdbcType(VarcharJdbcType.class)
    private String shortUrl;

}
