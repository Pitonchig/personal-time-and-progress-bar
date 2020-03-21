package net.thumbtack.ptpb.db;


import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.ClientPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.core.AerospikeTemplate;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableAerospikeRepositories(basePackages = {"net.thumbtack.ptpb.db"})
@EnableAutoConfiguration
@EnableTransactionManagement
@RequiredArgsConstructor
public class DbConfiguration {

    private final DbProperties dbProperties;

    @Bean(destroyMethod = "close")
    public AerospikeClient aerospikeClient() {
        ClientPolicy policy = new ClientPolicy();
        policy.failIfNotConnected = true;
        return new AerospikeClient(policy, dbProperties.getHost(), dbProperties.getPort());
    }

    @Bean
    public AerospikeTemplate aerospikeTemplate() {
        return new AerospikeTemplate(aerospikeClient(), dbProperties.getName());
    }

}
