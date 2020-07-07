package WordMoversDistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import WordMoversDistance.emd.EarthMovers;

/**
 * Created by Majer on 21.9.2016.
 */
public class WordMovers {
    
    private static final double DEFAULT_STOPWORD_WEIGHT = 0.5;
    private static final double DEFAULT_MAX_DISTANCE    = 1;
    
    private WordVectors wordVectors;
    private Set<String> stopwords;
    private double      stopwordWeight;
    
    private EarthMovers earthMovers;
    
    public WordMovers(Builder builder) {
        this.wordVectors = builder.wordVectors;
        this.stopwords = builder.stopwords;
        this.stopwordWeight = builder.stopwordWeight;
        this.earthMovers = new EarthMovers();
    }
    
    public double distance(String a, String b) {
        
        if(a.isEmpty() || b.isEmpty())
            throw new IllegalArgumentException();
        
        return distance(a.split(" "), b.split(" "));
    }
    
    public double distance(String[] tokensA, String[] tokensB) {
        
        if(tokensA.length < 1 || tokensB.length < 1)
            throw new IllegalArgumentException();
        
        Map<String, FrequencyVector> mapA = bagOfVectors(tokensA);
        Map<String, FrequencyVector> mapB = bagOfVectors(tokensB);
        
        if(mapA.size() == 0 || mapB.size() == 0) {
            throw new NoSuchElementException(
                    "Can't find any word vectors for given input text ..." + Arrays.toString(tokensA) + "|" +
                            Arrays.toString(tokensB));
        }
        //vocabulary of current tokens
        List<String> vocab = Stream.of(mapA.keySet(), mapB.keySet())
                                   .flatMap(Collection::stream)
                                   .distinct()
                                   .collect(Collectors.toList());
        double matrix[][] = new double[vocab.size()][vocab.size()];
        
        for(int i = 0 ; i < matrix.length ; i++) {
            String tokenA = vocab.get(i);
            for(int j = 0 ; j < matrix.length ; j++) {
                String tokenB = vocab.get(j);
                if(mapA.containsKey(tokenA) && mapB.containsKey(tokenB)) {
                    double distance = mapA.get(tokenA).getVector().distance2(mapB.get(tokenB).getVector());
                    //if tokenA and tokenB are stopwords, calculate distance according to stopword weight
                    if(stopwords != null && tokenA.length() != 1 && tokenB.length() != 1)
                        distance *= stopwords.contains(tokenA) && stopwords.contains(tokenB) ? 1 : stopwordWeight;
                    matrix[i][j] = distance;
                    matrix[j][i] = distance;
                }
            }
        }
        
        double[] freqA = frequencies(vocab, mapA);
        double[] freqB = frequencies(vocab, mapB);
        
        return earthMovers.distance(freqA, freqB, matrix, 0);
    }
    
    private Map<String, FrequencyVector> bagOfVectors(String[] tokens) {
        
        Map<String, FrequencyVector> map = new LinkedHashMap<>(tokens.length);
        Arrays.stream(tokens)
              .filter(x -> wordVectors.hasWord(x))
              .forEach(x -> map.merge(x, new FrequencyVector(wordVectors.getWordVectorMatrix(x)), (v, o) -> {
                  v.incrementFrequency();
                  return v;
              }));
        
        return map;
    }
    
    /*
    Normalized frequencies for vocab
     */
    private double[] frequencies(List<String> vocab, Map<String, FrequencyVector> map) {
        return vocab.stream().mapToDouble(x -> {
            if(map.containsKey(x)) {
                return (double) map.get(x).getFrequency() / map.size();
            }
            return 0d;
        }).toArray();
    }
    
    public static Builder Builder() {
        return new Builder();
    }
    
    public static final class Builder {
        
        public WordVectors wordVectors;
        public Set<String> stopwords;
        
        private double stopwordWeight = DEFAULT_STOPWORD_WEIGHT;
        
        private Builder() {}
        
        public WordMovers build() {
            return new WordMovers(this);
        }
        
        public Builder wordVectors(WordVectors wordVectors) {
            this.wordVectors = wordVectors;
            return this;
        }
        
        public Builder stopwords(Set<String> stopwords) {
            this.stopwords = stopwords;
            return this;
        }
        
        public Builder stopwordWeight(double stopwordWeight) {
            this.stopwordWeight = stopwordWeight;
            return this;
        }
        
    }
    
    private static WordVectors loadEmbeddings() {
    	return WordVectorSerializer.loadStaticModel(new File("C:\\Users\\saral\\Documents\\tfg\\Embeddings_2019-01-01\\Embeddings"
    			+ "\\Embeddings_ES\\Scielo\\50\\W2V_scielo_w10_c5_50_15epoch.txt"));
	}
    
    private static Set<String> loadStopWords() {
    	Set<String> list=new HashSet<String>();
    	File file= new File("C:\\Users\\saral\\Documents\\tfg\\Evaluation\\stopwords-es.txt");	
    	String charset = "UTF-8";
		Reader br;
		try {
			br = new InputStreamReader (new FileInputStream(file),charset);
			BufferedReader buffer= new BufferedReader(br);
			String linea;
			
			while((linea=buffer.readLine())!=null) {
				list.add(linea);
			}			
			buffer.close();
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return list;
	}
    
   
    
    public static void main(String[]args) {
    	Builder b= new Builder();
    	b.wordVectors=loadEmbeddings();
    	b.stopwords=loadStopWords();
    	b.stopwordWeight=DEFAULT_STOPWORD_WEIGHT;
    	WordMovers wmd=new WordMovers(b);
    	System.out.println(wmd.distance("resonancia magnetica nuclear","resonancia magnetica nuclear"));
    }

}
