package br.com.alura.owasp.retrofit;

import java.io.IOException;

import org.springframework.stereotype.Component;

import retrofit2.Call;

@Component
public class GoogleWebClient {
	
	private final static String SECRET = "6Lep4HQUAAAAAKPnTPWHO878yAX9p4y6E-Xn0Ns9";

	public boolean verifica(String recaptcha) throws IOException {
		
		Call<Resposta> token = new RetrofitInicializador().getGoogleService().enviaToken(SECRET, recaptcha);
		return token.execute().body().isSuccess();
	}

}
