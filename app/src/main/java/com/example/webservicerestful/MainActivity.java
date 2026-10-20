package com.example.webservicerestful;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.webservicerestful.api.ApiService;
import com.example.webservicerestful.api.RetrofitClient;
import com.example.webservicerestful.model.Report;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView tvReportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvReportsList = findViewById(R.id.tvReportsList);

        try {
            apiService = RetrofitClient.getApiService();
            fetchReportsFromServer();
        } catch (Exception e) {
            Log.e("INIT_ERROR", "Error al inicializar Retrofit: " + e.getMessage());
            tvReportsList.setText("Error al inicializar la API:\n" + e.getMessage());
        }
    }

    private void fetchReportsFromServer() {
        Call<List<Report>> call = apiService.getAllReports();

        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Report> reports = response.body();
                    Log.d("API_SUCCESS", "Retrieved " + reports.size() + " records.");
                    Toast.makeText(MainActivity.this,
                        "Successfully loaded " + reports.size() + " items.",
                        Toast.LENGTH_SHORT).show();

                    if (reports.isEmpty()) {
                        tvReportsList.setText("La respuesta del servidor está vacía (0 reportes).");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (Report r : reports) {
                            sb.append("ID: ").append(r.getId()).append("\n")
                              .append("Municipio: ").append(r.getMunicipality()).append("\n")
                              .append("Síntomas: ").append(r.getSymptomSummary()).append("\n")
                              .append("Riesgo: ").append(r.getRiskLevel()).append("\n\n");
                        }
                        tvReportsList.setText(sb.toString());
                    }
                } else {
                    String errorMsg = "Error del servidor: HTTP " + response.code();
                    Log.e("API_ERROR", errorMsg);
                    tvReportsList.setText(errorMsg + "\n\nAsegúrate de que Beeceptor tenga una regla GET /reports activa.");
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                String errorDetail = t.getMessage();
                Log.e("NETWORK_FAILURE", "Error de red/parsing: " + errorDetail, t);

                if (errorDetail != null && (errorDetail.contains("malformed JSON") || errorDetail.contains("setLenient") || errorDetail.contains("BEGIN_ARRAY"))) {
                    tvReportsList.setText("Error de formato JSON");
                } else {
                    tvReportsList.setText("Fallo de red:\n" + errorDetail + "\n\nVerifica tu conexión a internet o la URL de la API.");
                }

                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
