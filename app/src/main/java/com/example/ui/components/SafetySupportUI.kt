package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.safety.EmergencyResources
import com.example.data.safety.RiskLevel
import com.example.ui.theme.CreamWhite
import com.example.ui.theme.MutedBeige
import com.example.ui.theme.PaleVanilla
import com.example.ui.theme.SoftCharcoal
import com.example.ui.theme.WarmGrey

/**
 * Prominent Emergency Support UI matching the Solace+ Sanctuary safety specification.
 * Activates when risk level is LEVEL_4_CRITICAL.
 */
@Composable
fun EmergencySupportCard(
    modifier: Modifier = Modifier,
    country: String = "IN",
    onContactTrustedPerson: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val emergencyNumber = EmergencyResources.getPrimaryEmergencyNumber(country)
    val teleManasNumber = EmergencyResources.getPrimaryMentalHealthNumber(country)

    val terracottaColor = Color(0xFF9E5438)
    val emergencyRed = Color(0xFFD32F2F)
    val badgeBg = Color(0xFFFFECEB)
    val phoneGreen = Color(0xFF2E7D32)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PaleVanilla),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MutedBeige))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Solace AI icon + "Immediate support" badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Solace AI",
                        tint = terracottaColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Solace AI",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SoftCharcoal
                    )
                }

                // Immediate support badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Immediate support",
                        color = emergencyRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main heading
            Text(
                text = "You don't have to face this alone.",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SoftCharcoal,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your safety matters. Let's connect you with someone who can help right now.",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmGrey,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Card 1: Emergency services (112)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dialNumber(context, emergencyNumber) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CreamWhite),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MutedBeige))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Emergency services",
                            tint = phoneGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Emergency services",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = SoftCharcoal
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = emergencyNumber,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoal
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (country == "IN") "India — emergency assistance" else "Emergency assistance",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 2: Mental health support (14416 Tele-MANAS)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dialNumber(context, teleManasNumber) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CreamWhite),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MutedBeige))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Mental health support",
                            tint = emergencyRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Mental health support",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = SoftCharcoal
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = teleManasNumber,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoal
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (country == "IN") "Tele-MANAS" else "National Mental Health Lifeline",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 3: Can someone you trust stay with you?
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CreamWhite),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MutedBeige))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Can someone you trust stay with you?",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Contact a friend, family member or someone nearby who can be physically present.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (onContactTrustedPerson != null) {
                                onContactTrustedPerson()
                            } else {
                                openContactOptions(context)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = terracottaColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Contact a trusted person",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer disclaimer
            Text(
                text = "Solace+ is not an emergency service. Please seek immediate human assistance.",
                style = MaterialTheme.typography.labelSmall,
                color = WarmGrey.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Level 2 & Level 3 Support Banner embedded gracefully into the chat view.
 */
@Composable
fun SafetyScreeningSupportBanner(
    riskLevel: RiskLevel,
    onShowResources: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (riskLevel != RiskLevel.LEVEL_2_ELEVATED && riskLevel != RiskLevel.LEVEL_3_SUICIDAL_IDEATION) return

    val isLevel3 = riskLevel == RiskLevel.LEVEL_3_SUICIDAL_IDEATION
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PaleVanilla),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MutedBeige))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isLevel3) Color(0xFFFFECEB) else CreamWhite)
                        .border(1.dp, MutedBeige, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLevel3) Icons.Default.FavoriteBorder else Icons.Default.Call,
                        contentDescription = null,
                        tint = if (isLevel3) Color(0xFFD32F2F) else Color(0xFF9E5438),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isLevel3) "Confidential Support 24/7" else "Professional Care Available",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoal
                    )
                    Text(
                        text = if (isLevel3) "Tele-MANAS: 14416 • Free & 24/7" else "Speak with a licensed counsellor",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGrey,
                        fontSize = 11.sp
                    )
                }
            }

            Button(
                onClick = onShowResources,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9E5438),
                    contentColor = Color.White
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Help Lines",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun dialNumber(context: Context, number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) {}
}

private fun openContactOptions(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Hey, could you stay with me or chat for a bit right now?")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Reach out to a trusted person"))
        } catch (_: Exception) {}
    }
}
