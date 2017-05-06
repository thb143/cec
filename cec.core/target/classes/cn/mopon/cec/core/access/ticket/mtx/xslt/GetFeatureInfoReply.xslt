<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/GetCinemaPlanResult">
		<data>
			<ResultCode><xsl:value-of select="ResultCode" /></ResultCode>
			<shows>
				<xsl:for-each select="CinemaPlans/CinemaPlan">
					<Show>
						<featureAppNo><xsl:value-of select="FeatureAppNo" /></featureAppNo>
						<featureNo><xsl:value-of select="FeatureNo" /></featureNo>
					</Show>
				</xsl:for-each>
			</shows>
		</data>
	</xsl:template>
</xsl:stylesheet>