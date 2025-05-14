package tr.com.muskar.crypto_analysis_bot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tr.com.muskar.crypto_analysis_bot.model.entity.TradeRecord;

@Repository
public interface TradeRepository extends MongoRepository<TradeRecord, String> {
}
