<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data">
		<data>
			<result>0</result>
			<cinemas>
				<xsl:for-each select="cinemas/cinema">
					<cinema>
						<code><xsl:value-of select="@id" /></code>
						<name><xsl:value-of select="@name" /></name>
						<hallCount><xsl:value-of select="@hallCount" /></hallCount>
						<enabled>
							<xsl:choose>
									<xsl:when test="@status = '1'">true</xsl:when>
									<xsl:when test="@status = '0'">false</xsl:when>
									<xsl:otherwise></xsl:otherwise>
							</xsl:choose>
						</enabled>
						<halls>	
							<xsl:for-each select="hall">
								<hall>
									<code><xsl:value-of select="@id" /></code>
									<name><xsl:value-of select="@name" /></name>
								    <seatCount><xsl:value-of select="@seatcount" /></seatCount>
								</hall>
							</xsl:for-each>
						</halls>		
					</cinema>
				</xsl:for-each>
			</cinemas>
		</data>
	</xsl:template>
</xsl:stylesheet>